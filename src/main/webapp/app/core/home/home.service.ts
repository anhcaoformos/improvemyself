import axios from 'axios';
import buildPaginationQueryOpts from '@/shared/sort/sorts';
import type { IHome } from '@/shared/model/home.model';

const baseApiUrl = 'api/home';

export default class HomeService {
  public getDashboard(req?: any): Promise<IHome> {
    return new Promise<IHome>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}`, { params: buildPaginationQueryOpts(req) })
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
