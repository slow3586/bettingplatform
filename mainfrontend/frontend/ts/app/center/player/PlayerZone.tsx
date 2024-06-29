import React, {useState} from "react";
import {DataView} from "primereact/dataview";
import {ListBox} from "primereact/listbox";
import {Button} from "primereact/button";
import {useQuery} from "react-query";
import axios from "axios";

export function PlayerZone() {
    const [selectedChoice, setSelectedChoice] = useState(null);

    const gameQuery = useQuery(
        'gametype',
        async () => await axios.get('/game'),
        {
            enabled: false,
        });
    const game = gameQuery?.data?.data;

    const customerQuery = useQuery(
        'customer',
        async () => await axios.get('/customer'),
        {
            enabled: false,
        });
    const customer = customerQuery?.data?.data;

    const betHistoryQuery = useQuery(
        'history',
        async () => await axios.get('/bet'),
        {
            enabled: false,
        });
    const betHistory = betHistoryQuery?.data?.data;

    return (
        <div className="bp-player-zone">
            <DataView
                className="bp-player-state-history"
                value={betHistory}
                listTemplate={(items: any[]) =>
                    items.map((item) => (
                        <div className="bp-player-state-history-item" key={item.id}>
                            {item.createdAt}: {item.status}
                        </div>
                    ))
                }/>
            <div className="bp-player-state-overview">
                Balance: {customer.balance}
                <br/>
                Change: {customer.balance}
            </div>
            <div className="bp-player-input">
                <ListBox className="bp-player-input-value"
                         value={selectedChoice}
                         onChange={(e) => setSelectedChoice(e.value)}
                         options={[game.choice0, game.choice1, game.choice2]}
                         optionLabel="name"/>
                <div className="bp-player-input-buttons">
                    <Button className="bp-player-input-buttons-button"
                            severity="warning">BET</Button>
                </div>
            </div>
        </div>
    )
}