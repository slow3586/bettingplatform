import {createRoot} from "react-dom/client";
import React from "react";
import {QueryClient, QueryClientProvider} from 'react-query'
import {App} from "ts/app/App";

require('../less/index.less')

createRoot(document.getElementById("root")).render(
    <div>
        <QueryClientProvider client={new QueryClient()}>
            <App/>
        </QueryClientProvider>
    </div>
);