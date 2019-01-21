import Helper from '@ember/component/helper';

export default Helper.extend({
  compute(params) {
    let token = params[0];
    return token;
  }
});
