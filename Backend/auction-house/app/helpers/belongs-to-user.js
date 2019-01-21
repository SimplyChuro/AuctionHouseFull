import Helper from '@ember/component/helper';
import { inject as service } from '@ember/service';

export default Helper.extend({
  session: service(),
  
  compute(params) {
    let session = this.get('session');
    const user = params[0];
    if(user == session.userID) {
      return true;
    } else {
      return false;
    }
  }
});

