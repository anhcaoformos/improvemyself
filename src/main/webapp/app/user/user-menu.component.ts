import { defineComponent } from 'vue';
import { useI18n } from 'vue-i18n';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserMenu',
  setup() {
    const i18n = useI18n();
    return {
      t$: i18n.t,
    };
  },
});
