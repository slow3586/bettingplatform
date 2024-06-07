import React from "react";
import 'chartjs-adapter-date-fns';
import {Chart} from "primereact/chart";
import {useQueryClient} from "react-query";

export function GameZone() {
    const queryClient = useQueryClient();
    
    React.useEffect(() => {
        const websocket = new WebSocket('wss://127.0.0.1:8080/')
        websocket.onopen = () => {
            console.log('connected')
        }
        websocket.onmessage = (event) => {
            const data = JSON.parse(event.data)
            queryClient.setQueriesData(
                data.entity,
                (oldData) => {
                    const update = (entity: any) =>
                        entity.id === data.id
                            ? {...entity, ...data.payload}
                            : entity
                    return Array.isArray(oldData)
                        ? oldData.map(update)
                        : update(oldData)
                })
        }

        return () => {
            websocket.close()
        }
    }, [queryClient])

    return (
        <div className="bp-betting-chart-container">
            <Chart
                className="bp-betting-chart"
                type="line"
                data={{
                    datasets: [{
                        data: [
                            {x: '2024-06-04 20:00:00', y: 12760},
                            {x: '2024-06-04 20:00:01', y: 12420},
                            {x: '2024-06-04 20:00:02', y: 12240},
                            {x: '2024-06-04 20:00:03', y: 12630},
                            {x: '2024-06-04 20:00:04', y: 12570},
                            {x: '2024-06-04 20:00:05', y: 12250}
                        ]
                    }],
                }}
                options={{
                    scales: {
                        x: {
                            type: 'time',
                            time: {
                                unit: 'minute'
                            }
                        }
                    },
                    responsive: true,
                    maintainAspectRatio: false
                }}/>
        </div>
    )
}