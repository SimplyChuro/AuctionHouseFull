import Helper from '@ember/component/helper';
import { isEmpty } from '@ember/utils';
import { inject as service } from '@ember/service';

export default Helper.extend({
  customSession: service(),

  compute(params) {
    let token = null;
    if(!isEmpty(params[0])) {
      token = params[0];
    } else {
      token = this.get('customSession').getAuthToken();
    }

    return token;
  }
});
