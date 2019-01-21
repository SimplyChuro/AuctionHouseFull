import { helper } from '@ember/component/helper';

export function toLowerCase(params) {
  var string = params[0];
  return string.toLowerCase();
}

export default helper(toLowerCase);
