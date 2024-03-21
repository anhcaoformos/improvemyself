/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SplitBookJoiner from './split-book-joiner.vue';
import SplitBookJoinerService from './split-book-joiner.service';
import AlertService from '@/shared/alert/alert.service';

type SplitBookJoinerComponentType = InstanceType<typeof SplitBookJoiner>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SplitBookJoiner Management Component', () => {
    let splitBookJoinerServiceStub: SinonStubbedInstance<SplitBookJoinerService>;
    let mountOptions: MountingOptions<SplitBookJoinerComponentType>['global'];

    beforeEach(() => {
      splitBookJoinerServiceStub = sinon.createStubInstance<SplitBookJoinerService>(SplitBookJoinerService);
      splitBookJoinerServiceStub.retrieve.resolves({ headers: {} });

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
          splitBookJoinerService: () => splitBookJoinerServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        splitBookJoinerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SplitBookJoiner, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(splitBookJoinerServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.splitBookJoiners[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(SplitBookJoiner, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(splitBookJoinerServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: SplitBookJoinerComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SplitBookJoiner, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        splitBookJoinerServiceStub.retrieve.reset();
        splitBookJoinerServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        splitBookJoinerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(splitBookJoinerServiceStub.retrieve.called).toBeTruthy();
        expect(comp.splitBookJoiners[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(splitBookJoinerServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        splitBookJoinerServiceStub.retrieve.reset();
        splitBookJoinerServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(splitBookJoinerServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.splitBookJoiners[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(splitBookJoinerServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        splitBookJoinerServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSplitBookJoiner();
        await comp.$nextTick(); // clear components

        // THEN
        expect(splitBookJoinerServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(splitBookJoinerServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
