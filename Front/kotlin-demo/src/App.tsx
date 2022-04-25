import React, {useEffect, useMemo, useState} from 'react';
import axios from 'axios'
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
import './App.css';
import {Point, Station} from "./pointModel";


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

function randomInRange(start: number, end: number) {
    return Math.floor(Math.random() * (end - start + 1) + start);
}

function getRandomInt(max: number) {
    return randomInRange(0, max);
}

const getData = async () => {

    const stationResponse = await axios.get<Station | string>("http://localhost:8080/stations");
    const pointResponse = await axios.get<Point | string>("http://localhost:8080/points");

    // The data is received in NdJSON, so it can be :
    // if 1 line the object
    // if N line a String of object separated by \n
    const stations: Array<Station> = []
    if (stationResponse.data instanceof Object) {
        stations.push(stationResponse.data)
    } else {
        stationResponse.data.split("\n")
            .filter(str => str !== null && str !== "")
            .map(splitString => JSON.parse(splitString) as Station)
            .forEach(station => stations.push(station))
    }

    // And for point we need to transform the instance into date
    const points: Array<Point> = [];
    if (pointResponse.data instanceof Object) {
        const point = pointResponse.data
        point.time = new Date(point.time)
        points.push(point)
    } else {
        pointResponse.data.split("\n")
            .filter(str => str !== null && str !== "")
            .map(splitString => JSON.parse(splitString) as Point)
            .map(point => {
                point.time = new Date(point.time);
                return point;
            })
            .forEach(station => points.push(station))
    }
    return {stations, points}
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

function App() {
    const [stations, setStations] = useState<Array<Station>>([]);
    const [points, setPoints] = useState<Array<Point>>([]);
    // At the start of the component we query the data with a refresh interval to get the newest dat
    useEffect(() => {
        const getInfo = async () => {
            setInterval(async args => {
                const data = await getData();
                setPoints(data.points);
                setStations(data.stations);
            }, 1000);
        }
        getInfo();
    }, [])

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
