/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import SplitBookDetailService from './split-book-detail.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { SplitBookDetail } from '@/shared/model/split-book-detail.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('SplitBookDetail Service', () => {
    let service: SplitBookDetailService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new SplitBookDetailService();
      currentDate = new Date();
      elemDefault = new SplitBookDetail(123, 0, 'AAAAAAA', 'AAAAAAA', currentDate, 'INCOME', 'AAAAAAA', 'JOINER');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            transactionDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault,
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a SplitBookDetail', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            transactionDate: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService,
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a SplitBookDetail', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a SplitBookDetail', async () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            description: 'BBBBBB',
            personName: 'BBBBBB',
            transactionDate: dayjs(currentDate).format(DATE_FORMAT),
            transactionType: 'BBBBBB',
            groupId: 'BBBBBB',
            joinerRole: 'BBBBBB',
          },
          elemDefault,
        );

        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService,
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a SplitBookDetail', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a SplitBookDetail', async () => {
        const patchObject = Object.assign(
          {
            amount: 1,
            transactionDate: dayjs(currentDate).format(DATE_FORMAT),
            groupId: 'BBBBBB',
            joinerRole: 'BBBBBB',
          },
          new SplitBookDetail(),
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService,
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a SplitBookDetail', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of SplitBookDetail', async () => {
        const returnedFromService = Object.assign(
          {
            amount: 1,
            description: 'BBBBBB',
            personName: 'BBBBBB',
            transactionDate: dayjs(currentDate).format(DATE_FORMAT),
            transactionType: 'BBBBBB',
            groupId: 'BBBBBB',
            joinerRole: 'BBBBBB',
          },
          elemDefault,
        );
        const expected = Object.assign(
          {
            transactionDate: currentDate,
          },
          returnedFromService,
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of SplitBookDetail', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a SplitBookDetail', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a SplitBookDetail', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
