import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { type IObjective } from '@/shared/model/objective.model';
import type { ObjectiveType } from '../../shared/model/enumerations/objective-type.model';

const baseApiUrl = 'api/user/ledger/objectives';
const resourcePrefixUrl = 'api/user/ledger';
const resourceSuffixUrl = 'objectives';

export default class UserObjectiveService {
  public find(id: number): Promise<IObjective> {
    return new Promise<IObjective>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl + `?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IObjective): Promise<IObjective> {
    return new Promise<IObjective>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IObjective): Promise<IObjective> {
    return new Promise<IObjective>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IObjective): Promise<IObjective> {
    return new Promise<IObjective>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieveBy(ledgerId: number, paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${resourcePrefixUrl}/${ledgerId}/${resourceSuffixUrl}` + `?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public deleteBy(id: number, ledgerId: number, type: ObjectiveType, paymentCategoryId?: number): Promise<any> {
    let options: {} = { ledgerId: ledgerId, type: type };
    if (paymentCategoryId) {
      options = { ...options, paymentCategoryId: paymentCategoryId };
    }
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${resourcePrefixUrl}/${ledgerId}/${resourceSuffixUrl}/${id}`, { params: options })
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
