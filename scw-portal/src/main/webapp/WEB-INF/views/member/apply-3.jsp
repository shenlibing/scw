<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form role="form" class="submitForm" style="margin-top: 20px;">
	<div class="form-group">
		<label for="exampleInputEmail1">验证码</label> 
		<input type="text" name="code"
			class="form-control" id="exampleInputEmail1"
			placeholder="请输入你邮箱中接收到的验证码">
	</div>
	<button type="button" onclick="javascript:;" class="btn btn-primary unusebtn">重新发送验证码</button>
	<button type="button"
		url="${ctp}/auth/success.html"
		class="btn btn-success unusebtn">申请完成</button>
	<button style="display: none;" class="btn btn-default unusebtn" url="${ctp}/auth/apply-3.html"></button>
</form>