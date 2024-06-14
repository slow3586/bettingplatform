import React from "react";
import {Splitter, SplitterPanel} from "primereact/splitter";
import {PublicZone} from "ts/app/center/public/PublicZone";
import {GameZone} from "ts/app/center/game/GameZone";
import {PlayerZone} from "ts/app/center/player/PlayerZone";

export function CenterZone() {
    return (
        <Splitter className="bp-center">
            <SplitterPanel
                size={65}
                minSize={50}>
                <Splitter layout="vertical">
                    <SplitterPanel
                        size={65}
                        minSize={50}>
                        <GameZone/>
                    </SplitterPanel>
                    <SplitterPanel
                        size={35}
                        minSize={25}>
                        <PlayerZone/>
                    </SplitterPanel>
                </Splitter>
            </SplitterPanel>
            <SplitterPanel
                size={35}
                minSize={25}>
                <PublicZone/>
            </SplitterPanel>
        </Splitter>
    )
}