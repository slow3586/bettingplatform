import React, {Dispatch, SetStateAction, useState} from "react";

export const AppContext: React.Context<AppContextType>
    = React.createContext(null);

export type AppContextType = {
    jwt: string,
    setJwt: Dispatch<SetStateAction<string>>
}

export const AppContextProvider = ({children}: any) => {
    const [jwt, setJwt] = useState(null);

    return (
        <AppContext.Provider value={{jwt, setJwt}}>
            {children}
        </AppContext.Provider>
    );
};