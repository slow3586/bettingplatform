var path = require('path');
const TerserPlugin = require("terser-webpack-plugin");
const StatoscopeWebpackPlugin = require('@statoscope/webpack-plugin').default;
const LodashModuleReplacementPlugin = require('lodash-webpack-plugin');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');

module.exports = {
    entry: './frontend/ts/Root.tsx',
    devtool: 'source-map',
    cache: true,
    mode: 'production',
    output: {
        path: path.resolve('./src/main/resources/static/'),
        filename: 'built/bundle.js',
        publicPath: '/',
        assetModuleFilename: 'built/asset/[hash][ext][query]'
    },
    optimization: {
        minimize: true,
        minimizer: [new TerserPlugin()],
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx|tsx|ts)$/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        'plugins': ['lodash']
                    }
                },
                exclude: /node_modules/,

            },
            {
                test: /\.(less)$/,
                use: [
                    'style-loader',
                    'css-loader',
                    'less-loader'
                ]
            },
            {
                test: /\.s[ac]ss|css$/i,
                use: [
                    "style-loader",
                    "css-loader",
                    "sass-loader",
                ],
            },
            {
                test: /\.png$/,
                use: ['url-loader?limit=100000']
            },
            {
                test: /\.(woff(2)?|ttf|eot)$/,
                type: 'asset/resource',
            },
            {
                test: /\.(ttf|otf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?|(jpg|gif)$/,
                use: ['file-loader']
            }
        ]
    },
    plugins: [
        //new StatoscopeWebpackPlugin(),
        new LodashModuleReplacementPlugin()
    ],
    resolve: {
        plugins: [new TsconfigPathsPlugin({})],
        extensions: ['.ts', '.tsx', '.js', '.less'],
    },
};