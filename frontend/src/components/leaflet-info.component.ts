import { Component, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { map, filter } from 'rxjs/operators';

import { MapHandlerService } from '../maphandler.service';
import { MasterState, DistrictForMap, MasterPrecinct, GeoRegion } from '../models/geometry';

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
    this.maphandler.currentFeature.subscribe((next:GeoRegion|null) => this.updateCurrentFeature(next));
  }

  private updateCurrentFeature(next:GeoRegion|null) {
    if(next == null) {
      this.isValid = false;
    } else {
      this.isValid = true;
      this.featureName = next.name;
      this.population = next.population;
      this.vote_fraction = this.formatVoteFraction(next.average_democrat_votes);

      if(next instanceof DistrictForMap) {
        this.featureType="District";
      } else if (next instanceof MasterPrecinct) {
        this.featureType="Precinct";
      } else if (next instanceof MasterState) {
        this.featureType="State";
      } else {
        this.featureType="UNKNOWN_FEATURE_TYPE";
        console.error("UNKNOWN FEATURE:", next)
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
