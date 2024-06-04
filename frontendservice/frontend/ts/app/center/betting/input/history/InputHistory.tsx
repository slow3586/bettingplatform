import React, {useState} from "react";
import {AppTop} from "ts/app/top/AppTop";
import {AppCenter} from "ts/app/center/AppCenter";
import {DataView} from "primereact/dataview";

export function InputHistory() {
    const [products, setProducts] = useState([
        {id: 1, name: "user_event_0"},
        {id: 2, name: "user_event_1"}]);

    const itemTemplate = (product: any) => {
        return (
            <div className="bp-betting-input-history-list-item" key={product.id}>
                PRODUCT
            </div>
        );
    };

    const listTemplate = (items: any[]) => {
        if (!items || items.length === 0) return null;

        let list = items.map((product) => {
            return itemTemplate(product);
        });

        return <div className="bp-betting-input-history-list">{list}</div>;
    };

    return (
        <div className="bp-betting-input-history">
            <DataView value={products} listTemplate={listTemplate}/>
        </div>
    )
}