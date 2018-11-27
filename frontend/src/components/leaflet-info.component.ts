import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { MapHandlerService } from '../maphandler.service';

@Component({
  selector: 'app-leaflet-info',
  template: `
<h5>Selected Precinct:</h5>
<div>{{ featureName | async }}</div>
<br>
<div>Population: {{ population | async }}</div>
<div>{{ vote_fraction | async }}</div>
  `
})

export class LeafletInfoComponent {
  featureName: Observable<string>;
  
  population: Observable<number>;
  vote_fraction: Observable<string>;

  constructor(private maphandler: MapHandlerService) {
    
    this.featureName = this.maphandler.currentFeature.pipe(
        map(e => e == null ? 
            "Nothing selected" : (e as any).feature.properties.precinct_name));

    this.population = this.maphandler.currentFeature
          .pipe(filter(e => e!=null))
          .pipe(map( (e:any) => e.feature.properties.population));

    this.vote_fraction = this.maphandler.currentFeature
          .pipe(filter(e => e!=null))
          .pipe(map( (e:any) => e.feature.properties.fraction_votes_dem))
          .pipe(map( (e:number) => {
            let val = e >= 0.5 ? e: 1 - e;
            let str = (''+ (val*100).toFixed(0)) + '% ' + (e>=0.5?"Democrat":"Republican");
            return str;
          }));
  }
}

/*
"GEOID10": "id",
"NAME10": "precinct_name",
"TOWN_NAME": "town_name",
"COUNTY_NAM": "county_name",

"INTPTLAT10": "lat",
"INTPTLON10": "lon",
"geometry": "geometry",

"POP100": "population",
"VAP": "voting_population",
"VOTES": "total_votes",
"AV": "fraction_votes_dem",
*/
