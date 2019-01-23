import Helper from '@ember/component/helper';
import { inject as service } from '@ember/service';

export default Helper.extend({
  customSession: service(),

  compute(params) {
    let session = this.get('customSession');
    let bids = params[0];
    var checker = false;
    
    bids.forEach((item) => {
      if(item.user.get('id') == session.getUserID()) {
        checker = true;
      }
    });

    return checker;
  }
});

