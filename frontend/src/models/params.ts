
export class FormValidationError extends Error {}


export class FunctionWeights{

  // all weights floats in 0-1
  wCompactness: number = 0.5;
  wPopulationEquality: number = 0.5;
  wPartisanFairness: number = 0.5;
}


//type AlgorithmType = "REGION_GROWING" | "SIM_ANNEALING";

export class SimParams {
  algorithm: "REGION_GROWING" | "SIM_ANNEALING";

  annealingTime: number | null; //int total number of steps to run for, 100 to 1million?
  annealingStartWith: "RANDOM" | "CURRENT" | null;

  regionGrowingMovesToCheck: number | null; //int >0

  functionWeights: FunctionWeights;

  constructor(public readonly state: string){
    this.algorithm = "SIM_ANNEALING";
    this.annealingTime = 10 * 1000;
    this.annealingStartWith = "CURRENT";

    this.regionGrowingMovesToCheck = 100; 

    this.functionWeights = new FunctionWeights();
  }

  validate() {
    function assert(x: boolean, msg: string) { 
      if (!x) { throw new FormValidationError(msg); }
    }


    if(this.algorithm == "REGION_GROWING") {
      assert(this.regionGrowingMovesToCheck != null, "movesToCheck is null");
      assert( (this.regionGrowingMovesToCheck!) > 0 , "movesToCheck must be >0");

    } else { // Simulated annealing
      assert(this.annealingTime != null, "annealingTime is null");
      assert(this.annealingStartWith != null, "annealingStartWith is null");
    }
  }


  serialize(): string {
    return JSON.stringify(this,null, "  ");
  }
}

