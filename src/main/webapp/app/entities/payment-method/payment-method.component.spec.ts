/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PaymentMethod from './payment-method.vue';
import PaymentMethodService from './payment-method.service';
import AlertService from '@/shared/alert/alert.service';

type PaymentMethodComponentType = InstanceType<typeof PaymentMethod>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PaymentMethod Management Component', () => {
    let paymentMethodServiceStub: SinonStubbedInstance<PaymentMethodService>;
    let mountOptions: MountingOptions<PaymentMethodComponentType>['global'];

    beforeEach(() => {
      paymentMethodServiceStub = sinon.createStubInstance<PaymentMethodService>(PaymentMethodService);
      paymentMethodServiceStub.retrieve.resolves({ headers: {} });

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
          paymentMethodService: () => paymentMethodServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paymentMethodServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PaymentMethod, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(paymentMethodServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.paymentMethods[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(PaymentMethod, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(paymentMethodServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: PaymentMethodComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PaymentMethod, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        paymentMethodServiceStub.retrieve.reset();
        paymentMethodServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        paymentMethodServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(paymentMethodServiceStub.retrieve.called).toBeTruthy();
        expect(comp.paymentMethods[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should not load a page if the page is the same as the previous page', () => {
        // WHEN
        comp.page = 1;

        // THEN
        expect(paymentMethodServiceStub.retrieve.called).toBeFalsy();
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        paymentMethodServiceStub.retrieve.reset();
        paymentMethodServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(paymentMethodServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.paymentMethods[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(paymentMethodServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        paymentMethodServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePaymentMethod();
        await comp.$nextTick(); // clear components

        // THEN
        expect(paymentMethodServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(paymentMethodServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
