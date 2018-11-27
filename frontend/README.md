Directory structure:

src/ contains source code
build/  typescript compiled output (scratch space)
static/ contains static assets, and also is target for app.bundle.js

TO RUN CODE: 
    npm install // first time

    npm run build // builds static/app.bundle.js
    
    or
    npm run autobuild // builds bundle, and automatically rebuilds when sources change
