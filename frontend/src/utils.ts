// Leaflet debugging memes
import * as L from 'leaflet';
(window as any).L = L;


function pickRandom<T>(arr: T[]): T {
    let n = arr.length;
    let index = Math.floor(Math.random() * n);
    return arr[n];
}


// NJ GEOJSON DATA: 
// https://opendata.arcgis.com/datasets/bedf46ee246f400b962285218acc4287_4.geojson



export function show(s: any){
    console.log(`type:${typeof s}, val:'${s}'`);
}


