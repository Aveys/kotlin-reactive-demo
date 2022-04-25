export type Point = {
    id: string,
    time: Date,
    temp: number,
    unit: string,
    wind: string,
    stationId: string
}

export type Station = {
    id: string,
    name: string,
    location: string
}