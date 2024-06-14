import React from "react";
import {useCookies} from "react-cookie";

export const AppContext: React.Context<AppContextType>
    = React.createContext(null);

export type AppContextType = {
    jwt: any
}

export const AppContextProvider = ({children}: any) => {
    const jwt = useCookies(['Authorization'])?.[0];
    return (
        <AppContext.Provider value={{jwt}}>
            {children}
        </AppContext.Provider>
    );
};