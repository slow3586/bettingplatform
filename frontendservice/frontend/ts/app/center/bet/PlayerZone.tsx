import React, {useState} from "react";
import {DataView} from "primereact/dataview";
import {ListBox} from "primereact/listbox";
import {Button} from "primereact/button";
import {useQuery} from "react-query";
import axios from "axios";

export function PlayerZone() {
    const [valueChoices, setValueChoices] = useState([
        {name: "100", value: "100"},
        {name: "200", value: "200"},
        {name: "300", value: "300"},
    ]);
    const [selectedChoice, setSelectedChoice] = useState(null);
    const [events, setEvents] = useState([
        {id: 1, date: "123", name: "user_event_0"},
        {id: 2, date: "123", name: "user_event_1"}]);
    const [historyTotal, setHistoryTotal] = useState({balance: 123, changeSession: 1});
    const {isLoading, isSuccess, isError, data, error, refetch} =
        useQuery('query-tutorials',
            async () => await axios.get('/bezkoder.com/tutorials'),
            {
                enabled: false,
                retry: 2
            });

    return (
        <div className="bp-player-zone">
            <DataView
                className="bp-player-state-history"
                value={events}
                listTemplate={(items: any[]) =>
                    items.map((item) => (
                        <div className="bp-player-state-history-item" key={item.id}>
                            {item.date}: {item.name}
                        </div>
                    ))
                }/>
            <div className="bp-player-state-overview">
                Balance: {historyTotal.balance}
                <br/>
                Change: {historyTotal.balance}
            </div>
            <div className="bp-player-input">
                <ListBox className="bp-player-input-value"
                         value={selectedChoice}
                         onChange={(e) => setSelectedChoice(e.value)}
                         options={valueChoices}
                         optionLabel="name"/>
                <div className="bp-player-input-buttons">
                    <Button className="bp-player-input-buttons-button"
                            severity="warning">BET</Button>
                </div>
            </div>
        </div>
    )
}