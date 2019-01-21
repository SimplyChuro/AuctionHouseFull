import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  compute(params) {
    let token = params[0];
    return token;
  }
});
