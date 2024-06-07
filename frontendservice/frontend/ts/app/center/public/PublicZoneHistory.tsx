import React, {useState} from "react";
import {DataView} from "primereact/dataview";

export function PublicZoneHistory() {
    const [history, setHistory] = useState([
        {id: 1, user: "user_0", text: "user_event_1123123"},
        {id: 2, user: "user_1", text: "user_event_2123123"}]);

    return (
        <div className="bp-events-history">
            {history.map((item: any) => (
                <div className="bp-events-history-item" key={item.id}>
                    {item.user}: {item.text}
                </div>
            ))}
        </div>
    )
}