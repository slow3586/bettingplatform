import {createRoot} from "react-dom/client";
import React from "react";
import {QueryClient, QueryClientProvider} from 'react-query'
import {App} from "ts/app/App";
import {StompSessionProvider} from "react-stomp-hooks";

require('../less/index.less')

createRoot(document.getElementById("root"))
    .render(<div><App/></div>);