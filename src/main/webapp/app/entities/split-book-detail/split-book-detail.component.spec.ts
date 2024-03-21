/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SplitBookDetail from './split-book-detail.vue';
import SplitBookDetailService from './split-book-detail.service';
import AlertService from '@/shared/alert/alert.service';

type SplitBookDetailComponentType = InstanceType<typeof SplitBookDetail>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SplitBookDetail Management Component', () => {
    let splitBookDetailServiceStub: SinonStubbedInstance<SplitBookDetailService>;
    let mountOptions: MountingOptions<SplitBookDetailComponentType>['global'];

    beforeEach(() => {
      splitBookDetailServiceStub = sinon.createStubInstance<SplitBookDetailService>(SplitBookDetailService);
      splitBookDetailServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          splitBookDetailService: () => splitBookDetailServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        splitBookDetailServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SplitBookDetail, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(splitBookDetailServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.splitBookDetails[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SplitBookDetail, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(splitBookDetailServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SplitBookDetailComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SplitBookDetail, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        splitBookDetailServiceStub.retrieve.reset();
        splitBookDetailServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        splitBookDetailServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(splitBookDetailServiceStub.retrieve.called).toBeTruthy();
        expect(comp.splitBookDetails[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(splitBookDetailServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        splitBookDetailServiceStub.retrieve.reset();
        splitBookDetailServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(splitBookDetailServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.splitBookDetails[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(splitBookDetailServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        splitBookDetailServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSplitBookDetail();
        await comp.$nextTick(); // clear components

        // THEN
        expect(splitBookDetailServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(splitBookDetailServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
