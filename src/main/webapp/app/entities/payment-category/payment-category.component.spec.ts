/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PaymentCategory from './payment-category.vue';
import PaymentCategoryService from './payment-category.service';
import AlertService from '@/shared/alert/alert.service';

type PaymentCategoryComponentType = InstanceType<typeof PaymentCategory>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PaymentCategory Management Component', () => {
    let paymentCategoryServiceStub: SinonStubbedInstance<PaymentCategoryService>;
    let mountOptions: MountingOptions<PaymentCategoryComponentType>['global'];

    beforeEach(() => {
      paymentCategoryServiceStub = sinon.createStubInstance<PaymentCategoryService>(PaymentCategoryService);
      paymentCategoryServiceStub.retrieve.resolves({ headers: {} });

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
          paymentCategoryService: () => paymentCategoryServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paymentCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PaymentCategory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(paymentCategoryServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.paymentCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PaymentCategory, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(paymentCategoryServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PaymentCategoryComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PaymentCategory, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        paymentCategoryServiceStub.retrieve.reset();
        paymentCategoryServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        paymentCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(paymentCategoryServiceStub.retrieve.called).toBeTruthy();
        expect(comp.paymentCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(paymentCategoryServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        paymentCategoryServiceStub.retrieve.reset();
        paymentCategoryServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(paymentCategoryServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.paymentCategories[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(paymentCategoryServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        paymentCategoryServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePaymentCategory();
        await comp.$nextTick(); // clear components

        // THEN
        expect(paymentCategoryServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(paymentCategoryServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
