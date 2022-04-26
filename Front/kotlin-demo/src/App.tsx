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
import './App.css';
import axios from 'axios'
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

    const [stationEventSource, setStationEventSource] = useState<EventSource>();
    const [pointEventSource, setPointEventSource] = useState<EventSource>();

    useEffect(() => {
        const getStation = async () => {
            const response = await axios.get("http://localhost:8080/stations");
            setStations(response.data);
        }
        getStation();
        setStationEventSource(prevState => {
            if (!prevState || prevState.readyState === prevState.CLOSED) {
                const eventSource = new EventSource(`http://localhost:8080/stations/subscribe`);
                eventSource.onmessage = (e) => {
                    const parsedData = JSON.parse(e.data) as Station;
                    setStations((stations) => {
                            if (stations.find(station => station.id === parsedData.id)) {
                                return [...stations].map((station) => {
                                    if (station.id === parsedData.id) {
                                        return parsedData;
                                    }
                                    return station;
                                })
                            } else {
                                stations.push(parsedData);
                                return stations;
                            }

                        }
                    );

                }
                return eventSource;
            } else {
                return prevState;
            }
        })
    }, [stationEventSource]);

    useEffect(() => {
        setPointEventSource(prevState => {
            if (!prevState || prevState.readyState === prevState.CLOSED) {
                const eventSource = new EventSource(`http://localhost:8080/points`);
                eventSource.onmessage = (e) => {
                    const parsedData = JSON.parse(e.data) as Point;
                    parsedData.time = new Date(parsedData.time);
                    setPoints((points) => {
                            if (points.find(point => point.id === parsedData.id)) {
                                return [...points].map((point) => {
                                    if (point.id === parsedData.id) {
                                        return parsedData;
                                    }
                                    return point;
                                })
                            } else {
                                points.push(parsedData);
                                return points;
                            }

                        }
                    );
                };
                return eventSource;
            } else {
                return prevState;
            }
        })
    }, [pointEventSource]);


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
