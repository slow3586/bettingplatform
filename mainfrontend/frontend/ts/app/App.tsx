import React from "react";
import {TopZone} from "ts/app/top/TopZone";
import {CenterZone} from "ts/app/center/CenterZone";
import {QueryClient, QueryClientProvider} from "react-query";
import {StompSessionProvider} from "react-stomp-hooks";
import {AppContextProvider} from "ts/app/AppContext";

export function App() {
    return (
        <AppContextProvider>
            <StompSessionProvider
                connectHeaders={{Authorization: "Bearer 123"}}
                url="ws://127.0.0.1:8084/ws">
                <QueryClientProvider client={new QueryClient()}>
                    <div className="bp-app-container">
                        <div className="bp-app">
                            <TopZone/>
                            <CenterZone/>
                        </div>
                    </div>
                </QueryClientProvider>
            </StompSessionProvider>
        </AppContextProvider>
    )
}