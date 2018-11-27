import 'core-js/es7/reflect';
import 'zone.js/dist/zone';

import './utils';

import { platformBrowserDynamic } 
                     from '@angular/platform-browser-dynamic';
import { AppModule } from './app.module';


(window as any).debug = AppModule;

platformBrowserDynamic().bootstrapModule(AppModule);

