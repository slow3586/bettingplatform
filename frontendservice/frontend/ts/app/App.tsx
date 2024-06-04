import React from "react";
import {AppTop} from "ts/app/top/AppTop";
import {AppCenter} from "ts/app/center/AppCenter";

export function App() {
    return (
        <div className="bp-app-container">
            <div className="bp-app">
                <AppTop/>
                <AppCenter/>
            </div>
        </div>
    )
}