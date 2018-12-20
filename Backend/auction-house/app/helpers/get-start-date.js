import { helper } from '@ember/component/helper';
import { isEmpty } from '@ember/utils';

export default Ember.Helper.extend({
  
  compute(params) {
    var startDate = params[0];
    var checkerDate;
    if(isEmpty(startDate)) {
      checkerDate = new Date();
      checkerDate.setMinutes(checkerDate.getMinutes() - checkerDate.getTimezoneOffset());

      return moment(checkerDate).format("DD/MM/YYYY");
    } else {
      checkerDate = new Date(startDate);
      checkerDate.setMinutes(checkerDate.getMinutes() - checkerDate.getTimezoneOffset());
      
      return moment(checkerDate).format("DD/MM/YYYY");
    }
  }

});