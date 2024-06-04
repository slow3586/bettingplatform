import React, {useState} from "react";
import {AppTop} from "ts/app/top/AppTop";
import {AppCenter} from "ts/app/center/AppCenter";
import {InputText} from "primereact/inputtext";
import {Slider} from "primereact/slider";
import {Button} from "primereact/button";

export function InputForms() {
    const [betValue, setBetValue] = useState(1);

    return (
        <div className="bp-betting-input-forms">
            <div className="bp-betting-input-forms-value">
                <InputText
                    value={betValue.toString()}
                    onChange={(e) => setBetValue(parseInt(e.target.value))}/>
                <Slider
                    value={betValue}
                    min={1}
                    max={10}
                    step={1}
                    onChange={(e) => setBetValue(e.value as number)}/>
            </div>
            <div className="bp-betting-input-forms-buttons">
                <Button className="bp-betting-input-forms-buttons-button"
                        severity="warning">SHORT</Button>
                <Button className="bp-betting-input-forms-buttons-button"
                        severity="success">LONG</Button>
            </div>
        </div>
    )
}