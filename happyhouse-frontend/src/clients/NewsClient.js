import axios from 'axios';

export default class NewsClient {

  constructor(serverUrl) {
    this.serverUrl = serverUrl;
    this.baseUrl = serverUrl + "/news"
  }

  /**
   * @return {Promise<{
   *    title: string,
   * pubDate: string,
   * link: string,
   * guid: string,
   * author: string,
   * thumbnail: string,
   * description: string,
   * content: string
   * }[]>}
   */
  async getNews() {
    const response = await axios.get(this.baseUrl);
    if (response.status === 200 && response?.data) {
      return response.data;
    }
    return {};
  }
}