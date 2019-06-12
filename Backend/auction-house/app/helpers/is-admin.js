import Helper from '@ember/component/helper';
import { isEqual } from '@ember/utils';
import { inject as service } from '@ember/service';

export default Helper.extend({

  customSession: service(),
  
  compute(params) {
    let session = this.get('customSession');
    let token = session.getAdminChecker();
    if(isEqual(token, true) || isEqual(token, 'true')) {
      return true;
    } else {
      return false;
    }
  }
  
});

