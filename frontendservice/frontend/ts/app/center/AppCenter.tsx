import React from "react";
import {Splitter, SplitterPanel} from "primereact/splitter";
import {CenterBetting} from "ts/app/center/betting/CenterBetting";
import {CenterEvents} from "ts/app/center/event/CenterEvents";

export function AppCenter() {
    return (
        <Splitter className="bp-app-center">
            <SplitterPanel
                size={65}
                minSize={50}>
                <CenterBetting/>
            </SplitterPanel>
            <SplitterPanel
                size={35}
                minSize={25}>
                <CenterEvents/>
            </SplitterPanel>
        </Splitter>
    )
}