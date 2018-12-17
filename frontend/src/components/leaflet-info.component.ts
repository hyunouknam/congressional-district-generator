import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { MapHandlerService, LayerBacker } from '../maphandler.service';
import { MasterDistrict } from '../models/geometry';

@Component({
  selector: 'app-leaflet-info',
  template: `
<div *ngIf="!isValid; else elseBlock">
  <h5>No feature selected</h5>
</div>
<ng-template #elseBlock>
  <h5>Selected {{featureType}}:</h5>
  <div>{{ featureName }}</div>
  <br>
  <div>Population: {{ population }}</div>
  <div>{{ vote_fraction }}</div>
</ng-template> 
  `
})

export class LeafletInfoComponent {
  isValid: boolean;
  featureType: string;
  featureName: string;
  
  population: number;
  vote_fraction: string;

  constructor(private maphandler: MapHandlerService) {
    this.maphandler.currentFeature.subscribe((next:LayerBacker|null) => this.updateCurrentFeature(next));
  }

  private updateCurrentFeature(next:LayerBacker|null) {
    if(next == null) {
      this.isValid = false;
    } else {
      this.isValid = true;
      this.featureName = next.name;
      this.population = next.initialData.population;
      this.vote_fraction = this.formatVoteFraction(next.initialData.average_democrat_votes);

      if(next instanceof MasterDistrict) {
        this.featureType="District";
      }
    }
  }

  private formatVoteFraction(frac: number) {
      let val = frac >= 0.5 ? frac: 1 - frac;

      let str = (''+ (val*100).toFixed(0)) + '% ' + (frac>=0.5?"Democrat":"Republican");
      return str;
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
