package com.icia.Taeumproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icia.Taeumproject.Dao.BoardDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class BoardService {
	@Autowired
	private BoardDao bDao;
}
