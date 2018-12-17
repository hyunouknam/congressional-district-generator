
export type GeoJson = any;

export type GeoDataJson = {
    geometry: GeoJson;
    population: number;
    dem_vote_fraction: number;
}



export type MasterDistrictInitialJson = {
    name: string;
    id: string;
    initialData: GeoDataJson;
}

export type MasterStateInitialJson = {
    name: string;
    id: string;
    districts: MasterDistrictInitialJson[];
}

export class GeoData {
    public constructor(
            public readonly geometry: GeoJson,
            public readonly population: number,
            public readonly dem_vote_fraction: number,
    ){}

    public static loadFromJSON(json: GeoDataJson) {
        return new GeoData(json.geometry, json.population, json.dem_vote_fraction);
    }
}





export class MasterDistrict {
    private constructor(
        public name: string, 
        public id: string, 
        public initialData: GeoData) {
    }

    public static loadFromInitialJson(json: MasterDistrictInitialJson){
        let geodat = GeoData.loadFromJSON(json.initialData);
        return new MasterDistrict(json.name, json.id, geodat);
    }
}

export class MasterState {
    public districts: MasterDistrict[];
    private constructor(public name: string, public id: string) {
        this.districts = [];
    }

    public static loadFromInitialJson(json: MasterStateInitialJson) {
        let ms = new MasterState(json.name, json.id);
        
        json.districts.forEach(distJson => {
            let md = MasterDistrict.loadFromInitialJson(distJson);
            ms.districts.push(md)
        })

        return ms
    }

    public toString() {
        return `<MasterDistrict: ${this.name}, ${this.id}, Districts:
    ${this.districts.length} of them
>`
    }
}
