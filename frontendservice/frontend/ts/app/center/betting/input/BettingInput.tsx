import React from "react";
import {Splitter, SplitterPanel} from "primereact/splitter";
import {InputForms} from "ts/app/center/betting/input/forms/InputForms";
import {InputHistory} from "ts/app/center/betting/input/history/InputHistory";

export function BettingInput() {
    return (
        <Splitter
            className="bp-betting-input">
            <SplitterPanel
                minSize={25}>
                <InputHistory/>
            </SplitterPanel>
            <SplitterPanel
                size={50}
                minSize={25}>
                <InputForms/>
            </SplitterPanel>
        </Splitter>
    )
}