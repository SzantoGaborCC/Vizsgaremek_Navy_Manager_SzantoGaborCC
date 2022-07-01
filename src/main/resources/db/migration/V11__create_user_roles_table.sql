CREATE TABLE public.user_role
(
    user_id BIGINT NOT NULL,
    "role" VARCHAR(255) NOT NULL,
    CONSTRAINT each_user_can_have_each_role_once UNIQUE (user_id, "role"),
    CONSTRAINT user_user_role_fk FOREIGN KEY (user_id) REFERENCES public.user(id)
);