import { Component, Input, EventEmitter } from '@angular/core';

import { SimParams, FunctionWeights } from '../models/params';

import { Options as SliderOptions } from 'ng5-slider';

import { ServerCommService } from '../servercomm.service';

@Component({
  selector: 'app-sim-params',
  template: `
<div>
  
  <div class="d-flex justify-content-between">
    <h2 class="m-0">Start New Simulation</h2>
    <button class="btn" (click)="expand_collapse()">Expand/Collapse</button>
  </div>
  <form  class="mt-3" [style.display]="collapsed? 'none': 'block'">

    <div class="form-group">
      <h4>State</h4>

      <div class="form-check ml-2">
        <input class="form-check-input" type="radio" name="state" 
          value="CT" [(ngModel)]="params.state">
        <label class="form-check-label">
            Connecticut</label>
      </div>

      <div class="form-check ml-2">
        <input class="form-check-input" type="radio" name="state" 
          value="NJ" [(ngModel)]="params.state">
        <label class="form-check-label">
            New Jersey</label>
      </div>

      <div class="form-check ml-2">
        <input class="form-check-input" type="radio" name="state" 
          value="NE" [(ngModel)]="params.state">
        <label class="form-check-label">
            Nebraska</label>
      </div>
    </div>

    <div class="form-group">
      <h4>Simulation Type</h4>

      <div class="form-check ml-2">
        <input class="form-check-input" type="radio" name="simType" 
          value="REGION_GROWING" [(ngModel)]="params.algorithm">
        <label class="form-check-label" 
              ngbTooltip="Put together districts by adding precincts one at a time">
            Region Growing</label>
      </div>

      <div class="form-check ml-2">
        <input class="form-check-input" type="radio" name="simType" 
          value="SIM_ANNEALING" [(ngModel)]="params.algorithm">
        <label class="form-check-label"
              ngbTooltip="Start with an existing map and shuffle precincts around to improve things">
            Simulated Annealing</label>
      </div>
    </div>


    <div class="form-group" [ngSwitch]="params.algorithm">
      <h4>Simulation Parameters</h4>

      <!-- ************* Simulated Annealing Params ************ --> 
      <ng-container *ngSwitchCase="'SIM_ANNEALING'">

        <!-- startsWith RANDOM | CURRENT --> 
        <div class="form-check ml-2">
          <input class="form-check-input" type="radio" name="annealingStartWith" 
            value="RANDOM" [(ngModel)]="params.annealingStartWith">
          <label class="form-check-label">Start With Random Districts</label>
        </div>
        <div class="form-check ml-2">
          <input class="form-check-input" type="radio" name="annealingStartWith" 
            value="CURRENT" [(ngModel)]="params.annealingStartWith">
          <label class="form-check-label">Start With Current Districts</label>
        </div>


        <div class="form-group d-flex align-items-center">
          <ng-template #annealingTimeTip>
          <p>How many steps to run the simulated annealing simulation for. Longer times should yield better results,
          but will take longer to finish</p>
          </ng-template>
            <label class="col-sm-3"
                  [ngbTooltip]="annealingTimeTip">Annealing Time</label>
            <ng5-slider class="col-sm-9"
                        [(value)]="params.annealingTime" 
                        [options]="annealingTimeOpts"
                        [manualRefresh]="sliderRefreshEventEmitter"></ng5-slider>
        </div>
      </ng-container>


      <!-- ************* Region growing params ************ --> 
      <ng-container *ngSwitchCase="'REGION_GROWING'">
        <div class="form-group d-flex align-items-center">

          <ng-template #regionGrowingMovesToCheckTip>
          <p>How many moves to try out each step. If very high, it will try all possible moves and see which
          one works best. If low, it will pick the best move out of a small number it tries, but 
          can run much faster</p>
          </ng-template>

            <label class="col-sm-3"
                  [ngbTooltip]="regionGrowingMovesToCheckTip">Moves To Check</label>
            <ng5-slider class="col-sm-9"
                        [(value)]="params.regionGrowingMovesToCheck" 
                        [options]="movesToCheckOpts"
                        [manualRefresh]="sliderRefreshEventEmitter"></ng5-slider>
        </div>

      </ng-container>
    </div>

    <!-- common params-->
      




    <!-- One slider for each component we define-->
    <h4>Objective Function Weights:</h4>
    <div class="form-group d-flex align-items-center" *ngFor="let component of components">
      <label class="col-sm-3" [ngbTooltip]="component.tooltip">
                  {{component.name}}</label>
      <ng5-slider class="col-sm-9"
                  [(value)]="params.functionWeights[component.id]" 
                  [options]="funcWeightOpts"
                  [manualRefresh]="sliderRefreshEventEmitter"></ng5-slider>
    </div>

    <button class="btn mr-2" (click)="submit()">Start New Simulation</button>
  </form>
</div>
`
})

export class SimParamsComponent {
  collapsed = true; //form is collapsible with a button

  components = [
    { id: "wCompactness",
      name: "Compactness",
      tooltip: "Avoids making stretched-out districts",
    },
    { id: "wPopulationEquality",
      name: "Population Equality",
      tooltip: "Tries to get districts to have equal population",
    },
    { id: "wPartisanFairness",
      name: "Partisan Fairness",
      tooltip: "Ensures that if X% of people in the state vote for a party, " + 
              "then X% of districts will have a majority for that party.",
    },
  ]

  params: SimParams;


  // ===== STUFF FOR SLIDERS
  
  //will be passed into sliders
  sliderRefreshEventEmitter = new EventEmitter<void>();

  //slider options for various sliders
  funcWeightOpts = {
    floor: 0,
    ceil: 1,
    step: 0.01,
  }

  annealingTimeOpts = {
    floor: 100,
    ceil: 10 * 1000 * 1000,
    step: 100,
    logScale: true,
    translate: (n:number) => n.toLocaleString(), //add commas to slider value
  }

  movesToCheckOpts = {
    floor: 5,
    ceil: 10000,
    step: 5,
    translate: (n:number) => n.toLocaleString(), //add commas to slider value
  }

  

  constructor(private servercomm: ServerCommService ) {
    this.params = new SimParams(); 
  }

  submit() {
    this.servercomm.reqStartSimulation(this.params)
  }


  // toggle collapsed
  expand_collapse() {
    this.collapsed = !this.collapsed;
    this.sliderRefreshEventEmitter.emit();
  }

}
