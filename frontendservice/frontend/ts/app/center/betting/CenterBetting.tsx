import React from "react";
import {Splitter, SplitterPanel} from "primereact/splitter";
import {BettingInput} from "ts/app/center/betting/input/BettingInput";
import {BettingChart} from "ts/app/center/betting/chart/BettingChart";

export function CenterBetting() {
    return (
        <Splitter
            className="bp-center-betting"
            layout="vertical">
            <SplitterPanel
                size={60}
                minSize={50}>
                <BettingChart/>
            </SplitterPanel>
            <SplitterPanel
                minSize={25}>
                <BettingInput/>
            </SplitterPanel>
        </Splitter>
    )
}