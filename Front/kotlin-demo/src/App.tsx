import React, {useEffect, useMemo, useState} from 'react';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import {Line} from 'react-chartjs-2';
// @ts-ignore
import {RSocketClient} from 'rsocket-core';
import RSocketWebsocketClient from 'rsocket-websocket-client';
import './App.css';
import {Point, Station} from "./pointModel";
import {ReactiveSocket} from "rsocket-types";
import {Single} from "rsocket-flowable";


ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

export const options = {
    responsive: true,
    plugins: {
        legend: {
            position: 'top' as const,
        },
        title: {
            display: true,
            text: 'Weather Line Chart',
        },
    },
};

async function createClient(url: string) {
    const client = new RSocketClient({
        setup: {
            // ms btw sending keepalive to server
            keepAlive: 60000,
            // ms timeout if no keepalive response
            lifetime: 180000,
            // format of `data`
            dataMimeType: 'application/json',
            // format of `metadata`
            metadataMimeType: 'message/x.rsocket.routing.v0',
        },
        transport: new RSocketWebsocketClient({
            url: url
        }),
    });

    return client.connect();
}


function randomInRange(start: number, end: number) {
    return Math.floor(Math.random() * (end - start + 1) + start);
}

function getRandomInt(max: number) {
    return randomInRange(0, max);
}

const prepareData = (stations: Array<Station>, points: Array<Point>) => {

    // We clean the point to display the data in a better shape
    const updatedPoint: Array<Point> = points.map(point => {
        point.time = new Date(
            point.time.getFullYear(),
            point.time.getMonth(),
            point.time.getDate(),
            // point.time.getHours()
        )
        return point;
    })

    // After the cleaning we construct the label of our chart
    const labels = updatedPoint.map(point => point.time.getTime())
        .filter((date, i, array) => array.indexOf(date) === i)
        .sort((a, b) => a - b)
        .map(date => new Date(date).toISOString());


    return {
        labels,
        datasets: stations.map(station => {
            // After the label creation we create the point in our line chart 
            const stationPoints = updatedPoint.filter(point => point.stationId === station.id);
            const shade1 = getRandomInt(255)
            const shade2 = getRandomInt(255)
            const shade3 = getRandomInt(255)
            return {
                label: station.name,
                data: labels.map(date => {
                    const point = stationPoints.find(sp => sp.time.toISOString() === date)
                    if (point) {
                        return point.temp;
                    } else {
                        return null;
                    }
                }),
                borderColor: `rgb(${shade1}, ${shade2}, ${shade3})`,
                backgroundColor: `rgba(${shade1}, ${shade2}, ${shade3}, 0.5)`,
            }
        })
    }
}

function mergeState<T extends { id: string }>(prevState: Array<T>, data: T): Array<T> {
    if (prevState.find((value: T) => value.id === data.id)) {
        return [...prevState].map(value => {
            if (value.id === data.id) {
                return data
            }
            return value;
        })
    } else {
        prevState.push(data)
        return prevState;
    }
}


function App() {
    const [stations, setStations] = useState<Array<Station>>([]);
    const [points, setPoints] = useState<Array<Point>>([]);
    const [socket, setSocket] = useState<ReactiveSocket<any, any>>()

    useEffect(() => {
        const getSocket = async () => {
            const rsocketSingle: Single<ReactiveSocket<any, any>> = await createClient("ws://localhost:8080");
            rsocketSingle.then(rsocket => setSocket(rsocket));
        };
        getSocket();
    }, []);

    useEffect(() => {
        if (socket) {
            socket.requestStream({
                data: "",
                metadata: "stations.get.all"
            }).subscribe(a => {
                const parsedData = JSON.parse(a.data) as Station;
                setStations(prevSet => mergeState<Station>(prevSet, parsedData));
            })
        }

    }, [socket]);

    useEffect(() => {
        if (socket) {
            socket.requestStream({
                data: "",
                metadata: "points.get.all"
            }).subscribe(a => {
                const parsedData = JSON.parse(a.data) as Point;
                setPoints(prevSet => mergeState<Point>(prevSet, parsedData));
            })
        }

    }, [socket]);


    // The function prepareData should only by replay if station or point is changed
    const data = useMemo(() => prepareData(stations, points), [stations, points])

    return (
        <div className="App">
            <header className="App-header">
                <p>Super station méteo</p>
                <Line options={options} data={data}/>
            </header>
        </div>
    );
}

export default App;
