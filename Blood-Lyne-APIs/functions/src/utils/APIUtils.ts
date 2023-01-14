const axios = require('axios');

export function getDonorTravelDistance(originLatitude : any, originLongitude : any, destinationLatitude : any, destinationLongitude : any): Promise<any> {
    const baseURL: string = 'https://maps.googleapis.com/maps/api/distancematrix/json?';
    const apiKey : string = '';
    const config = {
        method: 'get',
        url: `${baseURL}origins=${originLatitude}%2C${originLongitude}&destinations=${destinationLatitude}%2C${destinationLongitude}&units=imperial&key=${apiKey}`,
        headers: {}
    };
    return axios(config);
}
