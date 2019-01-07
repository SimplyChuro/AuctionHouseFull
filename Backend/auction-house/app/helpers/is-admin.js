import Helper from '@ember/component/helper';
import { isEqual } from '@ember/utils';

export default Helper.extend({
  compute(params) {
    let token = params[0];
    if(isEqual(token, true) || isEqual(token, 'true')) {
      return true;
    } else {
      return false;
    }
  }
});

