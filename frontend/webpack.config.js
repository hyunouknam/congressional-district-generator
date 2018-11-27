const webpack = require('webpack')
const path = require('path')

module.exports = function () {
    return {
        entry: './src/main.ts',
        module: {
            rules: [
                {
                    test: /\.tsx?$/,
                    use: 'ts-loader',
                    exclude: /node_modules/
                }
            ]
        },
        resolve: {
            extensions: [ '.tsx', '.ts', '.js' ]
        },
        output: {
            path: __dirname + '/static',
            filename: 'app.bundle.js'
        },
        mode: "development"
    };
}

