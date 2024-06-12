import {createRoot} from "react-dom/client";
import React from "react";
import {App} from "ts/app/App";

require('../less/index.less')

createRoot(document.getElementById("root"))
    .render(<div><App/></div>);