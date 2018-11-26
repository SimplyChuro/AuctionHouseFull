import { helper } from '@ember/component/helper';

export function formatDate(params) {
  var format = "DD MMMM YYYY";
  var date = params[0];

  return moment(date).format(format);
}

export default helper(formatDate);
