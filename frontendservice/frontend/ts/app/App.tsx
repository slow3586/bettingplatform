import React from "react";
import {TopZone} from "ts/app/top/TopZone";
import {CenterZone} from "ts/app/center/CenterZone";

export function App() {
    return (
        <div className="bp-app-container">
            <div className="bp-app">
                <TopZone/>
                <CenterZone/>
            </div>
        </div>
    )
}