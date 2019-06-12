import Helper from '@ember/component/helper';
import { inject as service } from '@ember/service';

export default Helper.extend({
  customSession: service(),
  
  compute(params) {
    let session = this.get('customSession');
    const user = params[0];
    if(user == session.getUserID()) {
      return true;
    } else {
      return false;
    }
  }
});

