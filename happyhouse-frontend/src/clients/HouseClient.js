import axios from 'axios';

export default class HouseClient {
  constructor(serverUrl) {
    this.serverUrl = serverUrl;
    this.baseUrl = serverUrl + "/house";
  }

  async getHouses(
    serviceType, localCode, dealYear, dealMonth, page, numOfRows
  ) {
    const params = {
      serviceType, localCode, dealYear, dealMonth, page, numOfRows
    };
    const response = await axios.get(this.baseUrl, { params });
    if (response.status === 200 && response?.data?.items) {
      return response.data;
    }
    return {};
  }
}